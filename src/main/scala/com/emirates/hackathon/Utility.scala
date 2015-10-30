package com.emirates.hackathon

import scala.collection.mutable.MutableList

import edu.arizona.sista.processors.Processor
import edu.arizona.sista.processors.corenlp.CoreNLPProcessor
import edu.arizona.sista.struct.DirectedGraphEdgeIterator
import net.liftweb.json._
import net.liftweb.json.Serialization.write

case class InputDataVO(val message: String, val tokens: Option[List[String]], val namedEntities: Option[List[String]],
    val posTags: Option[List[String]], val normalizedEntities: Option[List[String]])
case class FlightStatus(val flightNumber: String)
case class FlightSearch(val flightNumber: String, val origin: String, val destination: String, val deptDate: String, val arrivalDate: String)
case class OutputDataVO(val isFlightStatus: Boolean, val isFlightSearch: Boolean, val flightStatus: Option[FlightStatus], val flightSearch: Option[FlightSearch])
object Utility {
  // create the processor
    // any processor works here! Try FastNLPProcessor or BioNLPProcessor.
    val proc:Processor = new CoreNLPProcessor(withDiscourse = true)
  def convertRawTextToInputData(rawText: String): InputDataVO = {
     implicit val formats = DefaultFormats
    var inputDataVO: InputDataVO = null;
    
    // create the processor
    // any processor works here! Try FastNLPProcessor or BioNLPProcessor.
    //val proc:Processor = new CoreNLPProcessor(withDiscourse = true)
    
    // the actual work is done here
    val doc = proc.annotate(rawText)
      
    // you are basically done. the rest of this code simply prints out the annotations
    
    // let's print the sentence-level annotations
    var sentenceCount = 0
    
    var tokens = MutableList[String]()
    var namedEntities = MutableList[String]()
    var normalizedEntities = MutableList[String]()
    var posTags = MutableList[String]()
    
    for (sentence <- doc.sentences) {
      //println("Sentence #" + sentenceCount + ":")
      //println("Tokens: " + sentence.words.mkString(" "))
      //println("Start character offsets: " + sentence.startOffsets.mkString(" "))
      //println("End character offsets: " + sentence.endOffsets.mkString(" "))
    
      // these annotations are optional, so they are stored using Option objects, hence the foreach statement
      //sentence.lemmas.foreach(lemmas => println("Lemmas: " + lemmas.mkString(" ")))
      //sentence.tags.foreach(tags => println("POS tags: " + tags.mkString(" ")))
      //sentence.entities.foreach(entities => println("Named entities: " + entities.mkString(" ")))
      //sentence.norms.foreach(norms => println("Normalized entities: " + norms.mkString(" ")))
      for (e <- sentence.words)
        tokens += e
      for (e <- sentence.tags.get)
        posTags += e
      for (e <- sentence.entities.get)
        namedEntities += e
      for (e <- sentence.norms.get)
        normalizedEntities += e
      sentence.dependencies.foreach(dependencies => {
        //println("Syntactic dependencies:")
        val iterator = new DirectedGraphEdgeIterator[String](dependencies)
        while(iterator.hasNext) {
          val dep = iterator.next
          // note that we use offsets starting at 0 (unlike CoreNLP, which uses offsets starting at 1)
          //println(" head:" + dep._1 + " modifier:" + dep._2 + " label:" + dep._3)
        }
      })
      sentence.syntacticTree.foreach(tree => {
        //println("Constituent tree: " + tree)
        // see the edu.arizona.sista.utils.Tree class for more information
        // on syntactic trees, including access to head phrases/words
      })
    
      sentenceCount += 1
      println("\n")
    }
    tokens.foreach { x => x.toLowerCase() }
    inputDataVO = new InputDataVO(rawText, Some(tokens.toList), Some(namedEntities.toList), Some(posTags.toList), Some(normalizedEntities.toList))
    //println(write(namedEntities))
    //println(write(inputDataVO.namedEntities))
    // let's print the coreference chains
    doc.coreferenceChains.foreach(chains => {
      for (chain <- chains.getChains) {
        //println("Found one coreference chain containing the following mentions:")
        for (mention <- chain) {
          // note that all these offsets start at 0 too
          /*println("\tsentenceIndex:" + mention.sentenceIndex +
            " headIndex:" + mention.headIndex +
            " startTokenOffset:" + mention.startOffset +
            " endTokenOffset:" + mention.endOffset +
            " text: " + doc.sentences(mention.sentenceIndex).words.slice(mention.startOffset, mention.endOffset).mkString("[", " ", "]"))*/
        }
      }
    })
    
    // let's print the discourse tree
    doc.discourseTree.foreach(dt => {
      //println("Document-wide discourse tree:")
      //println(dt.toString())
    })
   
     println(write(inputDataVO))
    
         inputDataVO
  }
  def multiContains(tokens: List[String], keywords: List[String]): Boolean = {
      //val rawText = tokens.mkString(" ")
      (keywords.foldLeft(false)( _ || tokens.contains(_) ))
      keywords.exists(tokens.contains)
    }
  def extractInput(rawText: String): OutputDataVO = {
    val inputDAtaVO:  InputDataVO = Utility.convertRawTextToInputData(rawText);
    val flightStatusWords = List("flight", "flights", "status")
    val flightSearchWords = List("flight", "flights", "ticket", "book", "date", "return")
    
    var outputDataVO: OutputDataVO = null
    var flightStatus: Option[FlightStatus] = null
    var flightSearch: Option[FlightSearch] = null
    var isFlightStatus: Boolean = false
    var isFlightSearch: Boolean = false
    
    if (!inputDAtaVO.tokens.isEmpty) {
      if ((inputDAtaVO.tokens.size == 1 && inputDAtaVO.tokens.get(0).contains("ek")) || ( multiContains(inputDAtaVO.tokens.get, flightStatusWords)
          && (inputDAtaVO.tokens.get.exists { x => if (x.contains("ek")) true else false }))) {
        val flightNo = inputDAtaVO.tokens.get.find { x => x.contains("ek") }
        flightStatus = Some(FlightStatus(flightNo.get))
        isFlightStatus = true
        
       println("flight status.....................................");
      } else if (multiContains(inputDAtaVO.tokens.get, flightSearchWords)
          && inputDAtaVO.namedEntities.get.contains("LOCATION") && inputDAtaVO.namedEntities.get.contains("DATE")) {
        isFlightSearch = true
        
        /*for ( i <- inputDAtaVO.namedEntities.size) {
          
        }*/
        println("flight search.....................................");
      }
    } else {
      
    }
    
        outputDataVO = OutputDataVO(isFlightStatus, isFlightSearch, flightStatus, flightSearch)
    outputDataVO
  }
  
  def main(args: Array[String]): Unit = {
    // create a JSON string from the Person, then print it
    //implicit val formats = DefaultFormats
    //println(write(Utility.convertRawTextToInputData("John Smith went to Chennai from Los Angeles on January 10th, 2013.")));
    extractInput("show flight status of ek200")
    extractInput("flight to Bangkok on january 20")
    extractInput("flight to Bangkok on next Thursday")
    extractInput("flight to Bangkok on next Monday")
    /*val tokens = List("show", "flight", "status", "ek200")
    println(tokens.exists { x => if (x.contains("ek")) true else false })*/
  }
  
}
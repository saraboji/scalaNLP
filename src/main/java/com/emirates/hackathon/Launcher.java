package com.emirates.hackathon;
import java.util.Scanner;

import org.easyrules.api.RulesEngine;

import com.emirates.hackathon.vo.InputDataVO;

import static org.easyrules.core.RulesEngineBuilder.aNewRulesEngine;

public class Launcher {
	public static void main(String[] args) {

	}
	public static void rules(InputDataVO inputDataVO) {
		//System.out.println(inputDataVO.rawText);
		
		//inputDataVO.rawText = "modified";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Are you a friend of duke?[yes/no]:");
        String input = scanner.nextLine();

        /**
         * Declare the rule
         */
        HelloWorldRule helloWorldRule = new HelloWorldRule();

        /**
         * Set business data to operate on
         */
        helloWorldRule.setInput(input.trim());

        /**
         * Create a rules engine and register the business rule
         */
        RulesEngine rulesEngine = aNewRulesEngine().build();

        rulesEngine.registerRule(helloWorldRule);

        /**
         * Fire rules
         */
        rulesEngine.fireRules();

    }
}

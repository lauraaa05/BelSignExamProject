package bll;

import be.Operator;

import java.util.HashMap;
import java.util.Map;

public class OperatorManager{

        private Map<Integer, Operator> operatorMap;

        // Constructor initializes the operator map
        public OperatorManager() {
            this.operatorMap = new HashMap<>();


//            operatorMap.put(1001, new Operator());
//            operatorMap.put(1002, new Operator());
//            operatorMap.put(1003, new Operator());
        }

        // Method to get an operator by QR cdode ID
        public Operator getOperatorById(int id) {
            return operatorMap.get(id); // Returns null if not found
        }
    }

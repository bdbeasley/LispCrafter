package lispcrafter

import com.cc.lc.interpreter.Interpreter

class CrafterController {

    def index() {
        if(params.query != null) {
            Interpreter interpreter = new Interpreter((String) params.query);
            try {
                String result = interpreter.evaluate();
                flash.result = result;
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}

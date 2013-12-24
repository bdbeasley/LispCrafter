package lispcrafter

import com.cc.lc.interpreter.Interpreter

class LispController {

    def index() {}

    def createQuery() {
        log.info("createQuery called");
        if(null != params.query) {
            log.info("query: " + params.query);
            Interpreter interpreter = new Interpreter((String) params.query);
            try {
                String result = interpreter.evaluate();
                render(contentType: "text/json") {
                    [result: result]
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}

package example;

import com.amazonaws.services.lambda.runtime.Context; 
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class Hello {
    public String myHandler(String myInput, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("received : " + myInput);
        System.out.println("Welcome To AWS-Lambda Hello-World");
        return myInput;
    }
}
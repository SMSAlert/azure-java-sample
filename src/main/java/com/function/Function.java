package com.function;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.Optional;
import com.smsalert.*;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */
    @FunctionName("HttpExample")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) throws Exception {
        context.getLogger().info("Java HTTP trigger processed a request.");

        SendSms smsObj = new SendSms();
        
        //If you want to use api key uncomment this line.
        //String apikey = smsObj.setapi_key("YOUR_APIKEY_HERE");

        String username = smsObj.setusername("SMSALERT_USERNAME");//Enter your SMS Alert username.
        String password = smsObj.setpassword("SMSALERT_PASSWORD");//Enter your SMS Alert password.

        String api_url= smsObj.setapi_url("https://www.smsalert.co.in");

        // Parse query parameter
        final String query0 = request.getQueryParameters().get("sender");
        final String sender = request.getBody().orElse(query0);

        final String query1 = request.getQueryParameters().get("mobileno");
        final String mobileno = request.getBody().orElse(query1);

        final String query2 = request.getQueryParameters().get("message");
        final String message = request.getBody().orElse(query2);

        final String query3 = request.getQueryParameters().get("route");
        final String route = request.getBody().orElse(query3);


        if (sender==null || mobileno==null || message==null) {
            return request.createResponseBuilder(HttpStatus.OK).body("Please pass sender, mobileno, message and route on the query string or in the request body to send the SMS.").build();
        } 
        else{   
            smsObj.send_sms(sender,mobileno, message, route);
            return request.createResponseBuilder(HttpStatus.OK).body("SMS message("+message+") is sent to "+mobileno+".").build();
        }
    }
}

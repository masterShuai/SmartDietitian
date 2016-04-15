package cn.smartDietician.backEnd.protocol;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by Administrator on 2015/5/8.
 */
public class ResponseContent {
    private String result = "";
    private String code = "";
    private Object content;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public static ResponseContent makeResponse(String result, String code, Object contentObj) {
        ResponseContent obj = new ResponseContent();
        obj.setResult(result);
        obj.setCode(code);
        obj.setContent(contentObj);
        return obj;

    }

    public static ResponseContent makeSuccessResponse(Object contentObj) {
        return makeResponse("success", "", contentObj);
    }

    public static ResponseContent makeSuccessResponse(String code, Object contentObj) {
        return makeResponse("success", code, contentObj);
    }

    public static ResponseContent makeSuccessResponse() {
        return makeResponse("success", "", "");
    }

    public static ResponseContent makeFailResponse(Object contentObj) {
        return makeResponse("fail", "", contentObj);
    }

    public static ResponseContent makeFailResponse() {
        return makeResponse("fail", "", "");
    }

    public static ResponseContent makeFailResponse(String code, Object content) {
        return makeResponse("fail", code, content);
    }


    private static Object fromJson(String content, Class<?> typeofClass) {
        ObjectMapper objectMapper = new ObjectMapper();
        Object obj = null;
        try {
            obj = objectMapper.readValue(content, typeofClass);
            //content = objectMapper.writeValueAsString(contentObj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }


    private static String makeJson(Object contentObj) {
        String content = "";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            content = objectMapper.writeValueAsString(contentObj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    @JsonIgnore
    @Override
    public String toString() {
        return "ResponseContent{" +
                "result='" + result + '\'' +
                ", code='" + code + '\'' +
                ", content=" + content +
                '}';
    }
}

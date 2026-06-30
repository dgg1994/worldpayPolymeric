package com.polymeric.base;

import com.polymeric.constants.Constants;

/**
 * @author csz
 */
public class BaseApiService {

    //返回失败，可传msg
    public static ResponseBase setResultError(String msg) {
        return setResult(Constants.HTTP_RES_CODE_500, msg, null);
    }
    
    //返回失败，可传msg、code
    public static ResponseBase setResultError(Integer code,String msg) {
        return setResult(code, msg, null);
    }

    //返回成功，可传msg
    public static ResponseBase setResultSuccess(String msg) {
        return setResult(Constants.HTTP_RES_CODE_200, msg, null);
    }

    //返回失败，可传msg
    public ResponseBase setResultError(Object data, String msg) {
        return setResult(Constants.HTTP_RES_CODE_500, msg, data);
    }

    //返回成功，可传data值,msg
    public static ResponseBase setResultSuccess(Object data, String msg) {

        return setResult(Constants.HTTP_RES_CODE_200, msg, data);
    }

    //返回成功，可传data值
    public ResponseBase setResultSuccess(Object data) {
        return setResult(Constants.HTTP_RES_CODE_200, Constants.HTTP_RES_CODE_200_VALUE, data);
    }

    //返回成功，没有data值
    public ResponseBase setResultSuccess() {
        return setResult(Constants.HTTP_RES_CODE_200, Constants.HTTP_RES_CODE_200_VALUE, null);
    }

    //通用封装
    public static ResponseBase setResult(Integer code, String msg, Object data) {

        return new ResponseBase(code, msg, data);
    }


    public static ResponseBase resultByInt(int i) {
        return resultByInt(i, null, null);
    }

    public static ResponseBase resultByInt(int i, String errorMsg) {
        return resultByInt(i, null, errorMsg);
    }

    /**
     * @param i i>0 即成功
     */
    public static ResponseBase resultByInt(int i, Object data, String errorMsg) {
        if (i > 0) {
            return setResult(Constants.HTTP_RES_CODE_200, Constants.HTTP_RES_CODE_200_VALUE, data);
        } else {
            return setResult(
                    Constants.HTTP_RES_CODE_500,
                    errorMsg == null ? Constants.ERROR : errorMsg,
                    data);
        }
    }


}

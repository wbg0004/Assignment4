public class MessageModel {

    public static final int GET_PRODUCT = 100;
    public static final int PUT_PRODUCT = 101;

    public static final int GET_CUSTOMER = 200;
    public static final int PUT_CUSTOMER = 201;

    public static final int OPERATION_OK = 1000; // server responses!
    public static final int OPERATION_FAILED = 1001;

    public static final int LOGIN = 300;
    public static final int LOGIN_SUCCESS = 3001;
    public static final int LOGIN_WRONG_PASS = -3002;
    public static final int LOGIN_WRONG_USER = -3003;
    public static final int LOGOUT = 301;
    public static final int LOGOUT_SUCCESS = 3011;

    public int code;
    public String data;

    public MessageModel() {
        code = 0;
        data = null;
    }

    public MessageModel(int code, String data) {
        this.code = code;
        this.data = data;
    }
}
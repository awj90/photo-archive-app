package ibf2022.batch2.csf.backend.exceptions;

public class MongoDatabaseException extends Exception {
    public MongoDatabaseException(){
        super();
    }

    public MongoDatabaseException(String message){
        super(message);
    }
}

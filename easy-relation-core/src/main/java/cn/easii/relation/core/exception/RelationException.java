package cn.easii.relation.core.exception;

public class RelationException extends RuntimeException {

    public RelationException(String errorMessage) {
        super(errorMessage);
    }

    public RelationException(Throwable throwable) {
        super(throwable);
    }

}

package api;

public interface API {
    ApiCommand getCommand( String verb, String stemNoun );
}

package dto;

import java.util.List;

public class ListLanguagesResponse extends DataTransferObject {
    private List<String> languages;

    public ListLanguagesResponse(List<String> languages) {
        super("ListLanguagesResponse");
        this.languages = languages;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }
}

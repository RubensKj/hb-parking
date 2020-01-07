package br.com.hbparking.termoLocacao;

import org.zwobble.mammoth.DocumentConverter;

import java.io.File;
import java.io.IOException;

public class RentalTermToHTML {

    private Long id;
    private String title;
    private String htmlFromFile;
    private String rentalTermStatus;

    public RentalTermToHTML() {
    }

    public RentalTermToHTML(Long id, String title, String htmlFromFile, String rentalTermStatus) {
        this.id = id;
        this.title = title;
        this.htmlFromFile = htmlFromFile;
        this.rentalTermStatus = rentalTermStatus;
    }

    public static RentalTermToHTML of(RentalTerm rentalTerm, File doxc) throws IOException {
        return new RentalTermToHTML(rentalTerm.getId(), rentalTerm.getTitle(), new DocumentConverter().convertToHtml(doxc).getValue(), rentalTerm.getRentalTermStatus().name());
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getHtmlFromFile() {
        return htmlFromFile;
    }

    public String getRentalTermStatus() {
        return rentalTermStatus;
    }
}

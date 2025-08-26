package rh.analyst.com.model;

import java.util.Map;

public class Documento {

    private String conteudo;
    private Map<String, Object> payload;

    public Documento() {
    }

    public Documento(String id, String tipo, String conteudo, Map<String, Object> payload) {
        this.conteudo = conteudo;
        this.payload = payload;
    }


    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }
}

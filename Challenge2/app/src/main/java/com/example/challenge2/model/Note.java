package com.example.challenge2.model;

/**
 * Note - Classe que pretende representar os dados de uma Nota no contexto do projeto
 */
public class Note {

    /**
     * Título da Nota
     */
    private String title;

    /**
     * Texto da Nota
     */
    private String text;

    /**
     * Construtor que permite criar uma nova Nota apenas com o título
     *
     * @param title título da Nota
     */
    public Note(String title) {
        this.title = title;
    }

    /**
     * Construtor que permite criar uma nova Nota com o título e conteúdo
     *
     * @param title título da Nota
     * @param text  conteúdo textual da Nota
     */
    public Note(String title, String text) {
        this.title = title;
        this.text = text;
    }

    /**
     * Método que permite obter o título da Nota
     *
     * @return título da Nota
     */
    public String getTitle() {
        return title;
    }

    /**
     * Método que permite atualizar o título da Nota
     *
     * @param title novo título da Nota
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Método que permite obter o conteúdo textual da Nota
     *
     * @return conteúdo textual da Nota
     */
    public String getText() {
        return text;
    }

    /**
     * Método que permite atualizar o conteúdo textual da Nota
     *
     * @param text novo conteúdo textual da Nota
     */
    public void setText(String text) {
        this.text = text;
    }
}

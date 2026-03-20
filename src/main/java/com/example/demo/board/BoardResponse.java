package com.example.demo.board;

import lombok.Data;

public class BoardResponse {

    @Data
    public static class Min {
        private Integer id;
        private String title;
        private String username;

        public Min(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.username = board.getUser().getUsername();
        }
    }

    @Data
    public static class Detail {
        private Integer id;
        private String title;
        private String content;
        private String username;

        public Detail(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.username = board.getUser().getUsername();
        }
    }
}

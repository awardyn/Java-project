package com.wardyn.Projekt2.domains;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Search {
    private Integer searchBy;

    public Search(Integer searchBy) {
        this.searchBy = searchBy;
    }

    public Search() {}
}

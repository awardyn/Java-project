package com.wardyn.Projekt2.domains;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Search {
    private Long searchBy;

    public Search(Long searchBy) {
        this.searchBy = searchBy;
    }

    public Search() {}
}

package com.readme.app.view.activity

import com.readme.app.model.util.Validator
import org.junit.Test

class BookEditActivityTest extends GroovyTestCase {
    private String title;

    private String author;

    private Integer totalPages;

    private Integer actualPage;

    void setUp() {

    }

    @Test
    void testTitle(){

        title = "Livro Qualquer"

        boolean result = Validator.isTitleValid(title)

        assertTrue()
    }
}

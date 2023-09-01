package ru.nsu.ccfit.mikhalev.digital_library.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.nsu.ccfit.mikhalev.digital_library.model.dto.BookDto;
import ru.nsu.ccfit.mikhalev.digital_library.service.BookService;
import ru.nsu.ccfit.mikhalev.digital_library.service.BookServiceImp;
import ru.nsu.ccfit.mikhalev.digital_library.service.Mock;
import org.springframework.http.ResponseEntity;
import ru.nsu.ccfit.mikhalev.digital_library.util.ContextSpecialSymbols;
import ru.nsu.ccfit.mikhalev.digital_library.util.ContextValidation;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@Validated
@Tag(name = "api.digital-library.tag.name", description = "api.digital-library.tag.description")
@RequestMapping(value = "/digital_library", produces = APPLICATION_JSON_VALUE)
public class BookController {
    @Autowired
    @Qualifier("bookServiceImp")
    private BookService bookService;

    @Operation(summary = "api.digital-library.book.info")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "api-digital-library.return-code.ok",
            description = "api-digital-library.return-code.ok.description")
    })
    @GetMapping("/book/{title}")
    public ResponseEntity<BookDto> getInfo(@PathVariable @Size(min = ContextValidation.MIN_SIZE_WORD,
                                                               max = ContextValidation.MAX_SIZE_WORD) String title) {
        log.info("get book " + title + " info");
        return ResponseEntity.ok(bookService.getBookInfoByTitle(title.replace(ContextSpecialSymbols.SYMBOL_EMPTY,
                                                                              ContextSpecialSymbols.SYMBOL_UNDERLINE)));
    }

    @Operation(summary = "api.digital-library.book.add")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "api-digital-library.return-code.ok",
                     description = "api-digital-library.return-code.ok.description")
    })
    @PostMapping("/book/add")
    public ResponseEntity<Void> add(@Valid @RequestBody BookDto bookDto) {
        log.info("add new book " + bookDto);
        bookService.add(bookDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/book/edit")
    public ResponseEntity<Void> edit(@Valid @RequestBody BookDto bookDto) {
        log.info("edit book " + bookDto);
        Mock.editBook(bookDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/books/{page}")
    public void getBooksPage(@PathVariable @Min(ContextValidation.MIN_SIZE_PAGES) Integer page) {
        log.info("get books from page " + page);
    }

    @DeleteMapping("/book/{title}")
    public ResponseEntity<Void> delete(@PathVariable @Size(min = ContextValidation.MIN_SIZE_WORD,
                                                           max = ContextValidation.MAX_SIZE_WORD) String title) {
        log.info("delete book " + title);
        Mock.deleteBook(title.replace(ContextSpecialSymbols.SYMBOL_EMPTY,
                                      ContextSpecialSymbols.SYMBOL_UNDERLINE));
        return ResponseEntity.ok().build();
    }


}

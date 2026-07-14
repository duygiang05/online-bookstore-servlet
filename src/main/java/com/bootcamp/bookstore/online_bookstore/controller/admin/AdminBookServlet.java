package com.bootcamp.bookstore.online_bookstore.controller.admin;

import com.bootcamp.bookstore.online_bookstore.model.Author;
import com.bootcamp.bookstore.online_bookstore.model.Book;
import com.bootcamp.bookstore.online_bookstore.model.BookSearchFilter;
import com.bootcamp.bookstore.online_bookstore.model.Category;
import com.bootcamp.bookstore.online_bookstore.model.Publisher;
import com.bootcamp.bookstore.online_bookstore.service.AuthorService;
import com.bootcamp.bookstore.online_bookstore.service.BookService;
import com.bootcamp.bookstore.online_bookstore.service.CategoryService;
import com.bootcamp.bookstore.online_bookstore.service.PublisherService;
import com.bootcamp.bookstore.online_bookstore.util.BookSearchParams;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@MultipartConfig

@WebServlet("/admin/book")
public class AdminBookServlet extends HttpServlet {
    private BookService bookService = new BookService();
    private AuthorService authorService = new AuthorService();
    private CategoryService categoryService = new CategoryService();
    private PublisherService publisherService = new PublisherService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {

            if (action != null) {
                request.setAttribute("authors",
                        authorService.findAll());

                request.setAttribute("categories",
                        categoryService.findAll());

                request.setAttribute("publishers",
                        publisherService.findAll());
                if("edit".equals(action)){
                    int bookId = Integer.parseInt(request.getParameter("book_id"));
                    request.setAttribute("book",bookService.findById(bookId));
                }
                request.getRequestDispatcher("/book-form.jsp")
                        .forward(request, response);
                return;
            }

            BookSearchFilter filter = BookSearchParams.fromRequest(request);
            List<Book> books = filter.hasCriteria()
                    ? bookService.searchForAdmin(filter)
                    : bookService.findAllForAdmin();

            request.setAttribute("books", books);
            request.setAttribute("categories", categoryService.findAll());
            request.setAttribute("authors", authorService.findAll());
            request.setAttribute("publishers", publisherService.findAll());
            request.setAttribute("searching", filter.hasCriteria());
            request.setAttribute("searchAction", request.getContextPath() + "/admin/book");

            request.getRequestDispatcher("/book-list.jsp")
                    .forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            int bookId = Integer.parseInt(request.getParameter("book_id"));
            try {
                bookService.delete(bookId);
                response.sendRedirect(request.getContextPath() + "/admin/book");
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        String id = request.getParameter("book_id");
        String title = trim(request.getParameter("title"));
        String isbn = trim(request.getParameter("isbn"));
        String priceStr = trim(request.getParameter("price"));
        String stockStr = trim(request.getParameter("stock"));
        String description = request.getParameter("description");
        String publishedYearStr = trim(request.getParameter("publishedYear"));
        String authorIdStr = trim(request.getParameter("authorId"));
        String categoryIdStr = trim(request.getParameter("categoryId"));
        String publisherIdStr = trim(request.getParameter("publisherId"));

        Part imagePart = request.getPart("coverImage");
        String fileName = imagePart != null ? imagePart.getSubmittedFileName() : null;
        boolean hasNewImage = fileName != null && !fileName.isBlank();

        boolean isNew = id == null || id.isBlank();
        String validationError = validateBookInput(isNew, title, isbn, priceStr, stockStr,
                publishedYearStr, authorIdStr, categoryIdStr, publisherIdStr, hasNewImage);
        if (validationError != null) {
            forwardBookFormWithError(request, response, validationError, title, isbn, 0, 0,
                    description, 0, parseIntOrZero(authorIdStr), parseIntOrZero(categoryIdStr),
                    parseIntOrZero(publisherIdStr), isNew ? null : parseIntOrNull(id));
            return;
        }

        double price = Double.parseDouble(priceStr);
        int stock = Integer.parseInt(stockStr);
        int publishedYear = Integer.parseInt(publishedYearStr);
        int authorId = Integer.parseInt(authorIdStr);
        int categoryId = Integer.parseInt(categoryIdStr);
        int publisherId = Integer.parseInt(publisherIdStr);

        if (hasNewImage) {
            String uploadPath = getServletContext().getRealPath("/images");
            imagePart.write(uploadPath + File.separator + fileName);
        }

        Author author = new Author();
        author.setId(authorId);

        Category category = new Category();
        category.setId(categoryId);

        Publisher publisher = new Publisher();
        publisher.setId(publisherId);

        Book book = new Book();
        book.setTitle(title);
        book.setIsbn(isbn);
        book.setPrice(price);
        book.setStock(stock);
        book.setDescription(description);
        book.setPublishedYear(publishedYear);
        book.setAuthor(author);
        book.setCategory(category);
        book.setPublisher(publisher);

        try {
            if (isNew) {
                book.setCoverImage(fileName);
                bookService.add(book);
            } else {
                int bookId = Integer.parseInt(id);
                Book existing = bookService.findById(bookId);
                if (existing == null) {
                    response.sendRedirect(request.getContextPath() + "/admin/book");
                    return;
                }
                book.setId(bookId);
                book.setCoverImage(hasNewImage ? fileName : existing.getCoverImage());
                bookService.update(book);
            }
            response.sendRedirect(request.getContextPath() + "/admin/book");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private String validateBookInput(boolean isNew, String title, String isbn, String priceStr,
                                     String stockStr, String publishedYearStr,
                                     String authorIdStr, String categoryIdStr,
                                     String publisherIdStr, boolean hasNewImage) {
        if (title == null || title.isBlank()) {
            return "Vui lòng nhập tên sách.";
        }
        if (isbn == null || isbn.isBlank()) {
            return "Vui lòng nhập ISBN.";
        }
        if (priceStr == null || priceStr.isBlank()) {
            return "Vui lòng nhập giá sách.";
        }
        if (stockStr == null || stockStr.isBlank()) {
            return "Vui lòng nhập tồn kho.";
        }
        if (publishedYearStr == null || publishedYearStr.isBlank()) {
            return "Vui lòng nhập năm xuất bản.";
        }
        if (authorIdStr == null || authorIdStr.isBlank()) {
            return "Vui lòng chọn tác giả.";
        }
        if (categoryIdStr == null || categoryIdStr.isBlank()) {
            return "Vui lòng chọn thể loại.";
        }
        if (publisherIdStr == null || publisherIdStr.isBlank()) {
            return "Vui lòng chọn nhà xuất bản.";
        }
        if (isNew && !hasNewImage) {
            return "Vui lòng chọn ảnh bìa sách.";
        }
        try {
            double price = Double.parseDouble(priceStr);
            if (price < 0) {
                return "Giá sách không hợp lệ.";
            }
        } catch (NumberFormatException e) {
            return "Giá sách không hợp lệ.";
        }
        try {
            int stock = Integer.parseInt(stockStr);
            if (stock < 0) {
                return "Tồn kho không hợp lệ.";
            }
        } catch (NumberFormatException e) {
            return "Tồn kho không hợp lệ.";
        }
        try {
            int year = Integer.parseInt(publishedYearStr);
            if (year < 1000 || year > 9999) {
                return "Năm xuất bản không hợp lệ.";
            }
        } catch (NumberFormatException e) {
            return "Năm xuất bản không hợp lệ.";
        }
        return null;
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private int parseIntOrZero(String value) {
        try {
            return value == null || value.isBlank() ? 0 : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private Integer parseIntOrNull(String value) {
        try {
            return value == null || value.isBlank() ? null : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void forwardBookFormWithError(HttpServletRequest request, HttpServletResponse response,
                                          String error, String title, String isbn, double price,
                                          int stock, String description, int publishedYear,
                                          int authorId, int categoryId, int publisherId,
                                          Integer bookId)
            throws ServletException, IOException {
        try {
            Book book = new Book();
            book.setTitle(title);
            book.setIsbn(isbn);
            book.setPrice(price);
            book.setStock(stock);
            book.setDescription(description);
            book.setPublishedYear(publishedYear);
            Author author = new Author();
            author.setId(authorId);
            book.setAuthor(author);
            Category category = new Category();
            category.setId(categoryId);
            book.setCategory(category);
            Publisher publisher = new Publisher();
            publisher.setId(publisherId);
            book.setPublisher(publisher);
            if (bookId != null) {
                book.setId(bookId);
            }

            request.setAttribute("error", error);
            request.setAttribute("book", book);
            request.setAttribute("authors", authorService.findAll());
            request.setAttribute("categories", categoryService.findAll());
            request.setAttribute("publishers", publisherService.findAll());
            request.getRequestDispatcher("/book-form.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:forEach var="film" items="${films}">
    <div class="modal fade" id="film-edit-modal-${film.id}" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Edit Film</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form action="FilmEdit" method="post">
                        <input type="hidden" name="id" value="${film.id}">
                        Title: <input type="text" name="title" value="${film.title}" required class="form-control"><br>
                        Year: <input type="number" name="year" value="${film.year}" required class="form-control"><br>
                        Director: <input type="text" name="director" value="${film.director}" required class="form-control"><br>
                        Stars: <input type="text" name="stars" value="${film.stars}" required class="form-control"><br>
                        Review: <textarea name="review" required class="form-control">${film.review}</textarea><br>
                        <button type="submit" class="btn btn-success">Save</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</c:forEach>

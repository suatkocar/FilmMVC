<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="modal fade" id="film-add-modal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Add New Film</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form action="FilmAdd" method="post">
                    Title: <input type="text" name="title" required class="form-control"><br>
                    Year: <input type="number" name="year" required class="form-control"><br>
                    Director: <input type="text" name="director" required class="form-control"><br>
                    Stars: <input type="text" name="stars" required class="form-control"><br>
                    Review: <textarea name="review" required class="form-control"></textarea><br>
                    <button type="submit" class="btn btn-success">Add Film</button>
                </form>
            </div>
        </div>
    </div>
</div>

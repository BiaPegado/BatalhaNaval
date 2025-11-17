module BatalhaNaval {
	exports br.ufrn.imd.model;

	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;

	opens application to javafx.graphics, javafx.fxml;
	opens br.ufrn.imd.control to javafx.fxml;
}

$(document).ready(function() {
	changePageAndSize();
});

/**
 * Esta función trocea los parámetros de la URL que actualmente hay cargada en
 * esta pestaña del navegador, y nos permite obtener el valor de un de ellos en
 * función de su nombre
 */
$.urlParam = function(name) {
	var results = new RegExp('[\?&]' + name + '=([^&#]*)')
			.exec(window.location.href);
	if (results == null) {
		return null;
	} else {
		return results[1] || 0;
	}

}

// Permite recargar la página cada vez que cambia el tamaño de página
function changePageAndSize() {
	$('#pageSizeSelect').change(function(evt) {

		// Url base
		var urlBase = "/misReservas/?";
		// Establece el tamaño de página recién seleccionado
		var pageSize = "pageSize=" + this.value;
		// Siempre que se cambia el tamaño de página, nos vamos a la página 1
		var page = "&page=1";
		// Comprobamos si se ha realizado una búsqueda, verificando
		// si en la url hay un parámetro llamado nombre. Si lo hay
		// lo volvemos a incluir en la url como parámetro, y si no
		// no hacemos nada
		var nombre = $.urlParam('nombre');
		var strnombre = "";
		if (nombre != null)
			strnombre = "&nombre=" + nombre;

		window.location.replace(urlBase + pageSize + page + strnombre);

	});
}
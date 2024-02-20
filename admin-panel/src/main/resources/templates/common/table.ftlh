<#macro table name order = "desc">

    <link rel="stylesheet" href="static/css/dataTables.bootstrap.css">
    <script src="static/js/jquery.dataTables.js"></script>
    <script src="static/js/dataTables.responsive.js"></script>
    <script src="static/js/dataTables.bootstrap.js"></script>

    <script>
        let language = {
            "decimal":        "",
            "emptyTable":     "В таблице нет данных",
            "info":           "Показано от _START_ до _END_ из _TOTAL_ объектов",
            "infoEmpty":      "Показано от 0 до 0 из 0 объектов",
            "infoFiltered":   "(из _MAX_)",
            "infoPostFix":    "",
            "thousands":      ",",
            "lengthMenu":     "Показать _MENU_ объектов",
            "loadingRecords": "Загрузка...",
            "processing":     "",
            "search":         "Поиск",
            "zeroRecords":    "Не найдено подходящих строк",
            "paginate": {
                "first":      "<<",
                "last":       ">>",
                "next":       ">",
                "previous":   "<"
            },
            "aria": {
                "orderable":  "Order by this column",
                "orderableReverse": "Reverse order this column"
            }
        }

        $(document).ready(function() {
            $('table#${name}').DataTable({
                "language": language,
                "order": [[ 0, "${order}" ]],
                "bStateSave": true,
                "bDeferRender": true,
                //"sPaginationType": "full_numbers",
                "oLanguage": {
                    "sLengthMenu": 'Показать <select class="form-control input-sm">'+
                        '<option value="10">10</option>'+
                        '<option value="25">25</option>'+
                        '<option value="50">50</option>'+
                        '<option value="100">100</option>'+
                        '<option value="500">500</option>'+
                        '<option value="-1">All</option>'+
                        '</select> объектов',
                    "oPaginate": {
                        "sFirst":    "<<",
                        "sLast":     ">>",
                        "sNext":     ">",
                        "sPrevious": "<"
                    }
                },
                "responsive": true
            });
        });
    </script>

</#macro>

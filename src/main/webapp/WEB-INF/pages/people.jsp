<jsp:include page="../includes/header.jsp"/>

<script src="https://cdn.datatables.net/1.10.18/js/jquery.dataTables.min.js"></script>
<link href="https://cdn.datatables.net/1.10.18/css/jquery.dataTables.min.css" rel="stylesheet" type="text/css">

<div class="wrapper">
    <jsp:include page="../includes/sidebar.jsp"/>

    <!-- Page Content Holder -->
    <div id="content">
        <jsp:include page="../includes/topbar.jsp"/>

        <div class="container-fluid">
            <table id="dataTable" class="display">
                <thead>
                <tr>
                    <th>First Name</th>
                    <th>Last Name</th>
                </tr>
                </thead>
                <tfoot>
                <tr>
                    <th>First Name</th>
                    <th>Last Name</th>
                </tr>
                </tfoot>
            </table>
        </div>
    </div>
</div>

<script>
    $(document).ready(function () {
        $('#dataTable').DataTable({
            "ajax": "https://kilwaz.me/peopleJSON",
            "columns": [
                {"data": "firstName"},
                {"data": "lastName"}
            ]
        });
    });
</script>

<jsp:include page="../includes/footer.jsp"/>
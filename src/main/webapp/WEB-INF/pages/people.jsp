<jsp:include page="../includes/header.jsp"/>

<script src="https://cdn.datatables.net/1.10.18/js/jquery.dataTables.min.js"></script>
<script src="https://resources.kilwaz.me/js/people.min.js"></script>
<link href="https://cdn.datatables.net/1.10.18/css/jquery.dataTables.min.css" rel="stylesheet" type="text/css">

<div class="wrapper">
    <jsp:include page="../includes/sidebar.jsp"/>

    <!-- Page Content Holder -->
    <div id="content">
        <jsp:include page="../includes/topbar.jsp"/>

        <div class="container-fluid">
            <div ng-app="peopleApp">
                <div ng-controller="peopleCtrl">
                    <div>
                        <form novalidate class="simple-form">
                            <div class="form-group">
                                <label for="firstNameInput">First Name</label>
                                <input type="text" class="form-control" id="firstNameInput"
                                       placeholder="Enter first name" ng-model="person.firstName">
                            </div>
                            <div class="form-group">
                                <label for="lastNameInput">Last Name</label>
                                <input type="text" class="form-control" id="lastNameInput"
                                       placeholder="Enter last name" ng-model="person.lastName">
                            </div>
                            <button id="addPerson" type="button" class="btn btn-primary" ng-click="createPerson(person)">Add</button>
                        </form>
                    </div>

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
    </div>
</div>

<jsp:include page="../includes/footer.jsp"/>
<%@ taglib prefix="cen" tagdir="/WEB-INF/tags" %>

<!-- Sidebar Holder -->
<nav id="sidebar">
    <div class="sidebar-header">
        <h3>Bootstrap Sidebar</h3>
        <strong>BS</strong>
    </div>

    <ul class="list-unstyled components">
        <li>
            <cen:internalLink classLink="pages.Hello" value="Hello" icon="fa fa-car"/>
        </li>
        <li>
            <cen:internalLink classLink="pages.DatabaseAdmin" value="Database Admin" icon="fa fa-car"/>
        </li>
        <li>
            <cen:internalLink classLink="pages.Import" value="Import" icon="fa fa-car"/>
        </li>
    </ul>
</nav>
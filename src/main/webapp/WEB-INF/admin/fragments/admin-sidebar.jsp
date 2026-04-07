<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<aside class="admin-sidebar">
    <div class="sidebar-brand">
        CarryCrew
    </div>
    <ul class="sidebar-menu">
        <li><a href="${pageContext.request.contextPath}/admin/dashboard"><i class="fas fa-chart-line"></i> Dashboard</a></li>
        <li><a href="${pageContext.request.contextPath}/admin/ordini"><i class="fas fa-box"></i> Ordini</a></li>
        <li><a href="${pageContext.request.contextPath}/admin/prodotti"><i class="fas fa-tshirt"></i> Prodotti</a></li>
        <li><a href="${pageContext.request.contextPath}/admin/categorie"><i class="fas fa-tags"></i> Categorie</a></li>
        <li><a href="${pageContext.request.contextPath}/admin/clienti"><i class="fas fa-users"></i> Clienti</a></li>
        <li><a href="${pageContext.request.contextPath}/admin/attributi"><i class="fas fa-sliders-h"></i> Attributi</a></li>
        <li><a href="${pageContext.request.contextPath}/admin/metodi-pagamento"><i class="fas fa-credit-card"></i> Metodi Pagamento</a></li>
        <li><a href="${pageContext.request.contextPath}/admin/team"><i class="fas fa-user-shield"></i> Team</a></li>
    </ul>
</aside>
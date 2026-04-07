<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<header class="admin-topbar">
	<div class="topbar-title">
		<h1>${pageTitle != null ? pageTitle : 'Panoramica'}</h1>
	</div>
	<div class="topbar-actions">
		<a href="${pageContext.request.contextPath}/admin/logout"
			class="btn-logout"> <i class="fas fa-sign-out-alt"></i> Esci
		</a>
	</div>
</header>
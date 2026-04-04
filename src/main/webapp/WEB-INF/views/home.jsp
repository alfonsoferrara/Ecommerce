<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home Page | CarryCrew</title>
        <link rel="icon" type="image/png" sizes="16x16" href="${pageContext.request.contextPath}/images/favicon-16x16.png">
    
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/homePage.css">
</head>
<body>

    <jsp:include page="fragments/header.jsp" />

    <main>
        <section class="hero-section">
            <div class="hero-content">
                <h1>Tutti i trolley da cabina. Tutti i top brand. Un solo shop.</h1>
                <p>Scopri la nuova collezione di trolley rigidi da cabina</p>
                <a href="${pageContext.request.contextPath}/categoria?id=1" class="btn-primary">Catalogo</a>
            </div>
        </section>

        <section class="products-section">
            <h2>I Più Richiesti</h2>
            <div class="product-grid">
                <c:forEach var="prodotto" items="${prodottiVetrina}">
                    <div class="product-card">
                        <c:choose>
                            <c:when test="${not empty immaginiPrincipali[prodotto.id]}">
									<img src="${pageContext.request.contextPath}${immaginiPrincipali[prodotto.id]}"
										alt="${prodotto.nome}" class="product-image"
										onerror="this.src='${pageContext.request.contextPath}/images/default.jpg'">
								</c:when>
								<c:otherwise>
									<img
										src="${pageContext.request.contextPath}/images/default.jpg"
										alt="${prodotto.nome}" class="product-image">
								</c:otherwise>
                        </c:choose>
                        
                        <h3>${prodotto.nome}</h3>
                        <p class="price">&euro; ${prodotto.prezzo}</p>
                        <a href="${pageContext.request.contextPath}/prodotto?id=${prodotto.id}" class="btn-outline">Acquista</a>
                    </div>
                </c:forEach>
                
                <c:if test="${empty prodottiVetrina}">
                    <p>Nessun prodotto disponibile in vetrina al momento.</p>
                </c:if>
            </div>
        </section>

        <section class="about-section">
            <div class="about-text">
                <h2>Chi Siamo</h2>
                <p>Siamo appassionati di viaggi, non solo di valigie. Per questo abbiamo riunito in un unico shop i migliori brand di trolley da cabina: per offrirti la massima scelta, senza rinunciare a chiarezza e affidabilità. Confronta, scegli e parti.</p>
                <br>
                <a href="#" class="btn-outline">Leggi la nostra storia</a>
            </div>
            <div class="about-image">
                <img src="https://images.unsplash.com/photo-1670888665009-11b5b47c59de?q=80&w=1171&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D" alt="Il nostro team al lavoro">
            </div>
        </section>
    </main>

    <jsp:include page="fragments/footer.jsp" />

    <script src="${pageContext.request.contextPath}/scripts/main.js"></script>
    
</body>
</html>
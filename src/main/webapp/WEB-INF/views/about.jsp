<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi Siamo | CarryCrew</title>
    
    <link rel="icon" type="image/png" sizes="16x16" href="${pageContext.request.contextPath}/images/favicon-16x16.png">
    
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/about.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/fragments/header.jsp" />

    <main class="about-page-container">
        
        <section class="about-hero">
            <h1>La Nostra Visione</h1>
            <p>Siamo CarryCrew. Nati con un'idea semplice ma potente: unire un design essenziale, dominato dal nero, a una qualità senza compromessi. Non seguiamo le mode, le creiamo.</p>
        </section>

        <section class="about-content-section">
            
            <div class="about-text-block">
                <h2>Dove ci troviamo</h2>
                <p>Il nostro quartier generale è il luogo dove nascono le idee e prendono forma le collezioni. Operiamo in uno spazio moderno, pensato per stimolare la creatività del nostro team e gestire al meglio le spedizioni verso i nostri clienti.</p>
                <p>Vuoi collaborare con noi o hai bisogno di assistenza? Vieni a trovarci o utilizza i nostri canali di contatto. Siamo sempre pronti ad ascoltarti.</p>
                
                <div class="contact-details">
                    <p><i class="fas fa-map-marker-alt"></i> Via Roma 1 - 00100 Roma (RM)</p>
                    <p><i class="fas fa-envelope"></i> info@carrycrew.it</p>
                    <p><i class="fas fa-phone"></i> +39 06 12345678</p>
                </div>
            </div>

            <div class="about-map-block">
                <iframe 
                    src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d593293.6313449368!2d12.01270816224123!3d41.65156943239203!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x132f6196f9928ebb%3A0xb90f770693656e38!2sRoma%20RM!5e1!3m2!1sit!2sit!4v1775546734543!5m2!1sit!2sit" 
                    width="600" 
                    height="450" 
                    style="border:0;" 
                    allowfullscreen="" 
                    loading="lazy" 
                    referrerpolicy="no-referrer-when-downgrade">
                </iframe>
            </div>

        </section>

    </main>

    <jsp:include page="/WEB-INF/views/fragments/footer.jsp" />

    <script src="${pageContext.request.contextPath}/scripts/main.js"></script>

</body>
</html>
document.addEventListener('DOMContentLoaded', function() {
    const token = localStorage.getItem('token');
    if (token) {
        document.getElementById('loginbotao').style.display = 'none';
        console.log('token checado');
    } else {
        window.location.href = '/Codigo/CodigosFrontEnd/Login/novologin.html';
    }
});
document.addEventListener('DOMContentLoaded', function() {
    const token = localStorage.getItem('token');
    const userId = localStorage.getItem('id'); // Supondo que o ID do usuário esteja armazenado no localStorage

    if (!token) {
        window.location.href = '/Codigo/CodigosFrontEnd/Login/novologin.html';
        return;
    }

    fetch(`http://localhost:6789/usuario/checkBanda?id=${userId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else if (response.status === 404) {
            console.log("Usuário não está em uma banda.");
            //mensagem perguntando para onde ele quer ir
            window.location.href = '/Codigo/CodigosFrontEnd/cadastrodebanda/htmlteste.html';
            return null;
        } else {
            throw new Error('Erro na requisição: ' + response.statusText);
        }
    })
    .then(data => {
        if (data) {
            const nomeBanda = data.nomeBanda; // Supondo que a resposta contenha o nome da banda
            window.location.href = `/Codigo/CodigosFrontEnd/encontrebandas/banda.html?nomeBanda=${nomeBanda}`;
        }
    })
    .catch(error => {
        console.error('Erro ao verificar banda do usuário:', error);
    });
});
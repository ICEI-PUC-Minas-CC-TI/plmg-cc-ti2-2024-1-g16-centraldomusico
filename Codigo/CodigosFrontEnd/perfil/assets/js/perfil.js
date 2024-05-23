document.addEventListener('DOMContentLoaded', function () {
    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get('id');
    //esconder id da url
    window.history.replaceState({}, document.title, "/Codigo/CodigosFrontEnd/perfil/perfil.html");

    if (id) {
        fetch(`http://localhost:6789/usuario/get/perfil?id=${id}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro na requisição: ' + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            console.log('Resposta do servidor:', data);
            if (data) {
                document.getElementById('nome').textContent = data.nome;
                document.getElementById('descricao').textContent = data.descricao;
                document.getElementById('cache').textContent = data.cache;
                document.getElementById('instrumento1').textContent = data.instrumento1;
                document.getElementById('instrumento2').textContent = data.instrumento2;
                document.getElementById('instrumento3').textContent = data.instrumento3;
                document.getElementById('objetivo').textContent = data.objetivo;
                document.getElementById('estilo').textContent = data.estilo;
            }
        })
        .catch(error => {
            console.error('Erro ao buscar dados do usuário:', error);
            document.getElementById('msgError').textContent = 'Erro ao carregar os dados do usuário. Tente novamente.';
        });
    } else {
        document.getElementById('msgError').textContent = 'ID de usuário ausente na URL.';
    }
});

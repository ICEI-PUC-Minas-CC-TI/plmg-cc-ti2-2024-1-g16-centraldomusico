document.addEventListener('DOMContentLoaded', function () {
    const urlParams = new URLSearchParams(window.location.search);
    const id = localStorage.getItem('id');
    const token = localStorage.getItem('token');
    //armazenar id no local storage
    console.log('TOKEN:', token);
    // Esconder ID da URL
    window.history.replaceState({}, document.title, "/Codigo/CodigosFrontEnd/perfil/perfil.html");

    if (token) {
        console.log('token checado');
        console.log('ID:', id);
        fetch(`http://localhost:6789/usuario/get/perfil?id=${id}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
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
        document.getElementById('msgError').textContent = 'Token de autenticação ausente. Faça login novamente.';
    }
});

//criar funcao onclick para logout
function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('id');
    window.location.href = '/Codigo/CodigosFrontEnd/Login/novologin.html';
}

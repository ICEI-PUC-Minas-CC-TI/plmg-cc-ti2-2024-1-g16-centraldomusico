//criar id randomico
var id;
function entrar() {
    var usuario = document.getElementById('login').value;
    var senha = document.getElementById('senha').value;

    //método GET para fazer login
    fetch(`http://localhost:6789/usuario/get?usuario=${usuario}&senha=${senha}`, {
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
        id = data.id;
        window.location.href = `/Codigo/CodigosFrontEnd/perfil/perfil.html?id=${id}`;
    })
    .catch(error => {
        console.error('Erro ao fazer login:', error);
        document.getElementById('msgError').textContent = 'Usuário ou senha incorretos. Tente novamente.';
    });
}

function entrar(event) {
    event.preventDefault(); // Impede o envio padrão do formulário
    
    var usuario = document.getElementById('login').value;
    var senha = document.getElementById('senha').value;
    
    console.log('Usuário:', usuario);
    console.log('Senha:', senha);

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
        localStorage.setItem('token', data.token);
        localStorage.setItem('id', data.id);
        localStorage.setItem('secret', data.secret);
        alert('Seja bem vindo(a) a Central do Músico!');        
        window.location.href = `/Codigo/CodigosFrontEnd/perfil/perfil.html?id=${data.id}`;
    })
    .catch(error => {
        console.error('Erro ao fazer login:', error);
        console.log('Resposta do servidor:', error);
        document.getElementById('msgError').textContent = 'Usuário ou senha incorretos. Tente novamente.';
        alert('Usuário ou senha incorretos. Tente novamente.');
    });
}

var id;
function entrar() {
    const usuario = document.getElementById('login').value;
    const senha = document.getElementById('senha').value;

    fetch('https://jsonservercentraldomusico.arthurcarvalh19.repl.co/perfil')
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao obter os dados dos usuários');
            }
            return response.json();
        })
        .then(data => {
            const user = data.find(user => user.usuario === usuario && user.senha === senha);

            if (user) {
                alert('Login bem-sucedido');
                // Redirecionar o usuário para explorar.html no lado do servidor
                window.location.href = "/perfil/perfil.html?id=" + user.id;
                id = user.id;
            } else {
                document.getElementById('msgError').innerText = 'Credenciais inválidas';
            }
        })
        .catch(error => {
            console.error('Erro na requisição fetch:', error);
            document.getElementById('msgError').innerText = 'Erro de comunicação com o servidor';
        });

    return false; // Evita o envio do formulário tradicional
}



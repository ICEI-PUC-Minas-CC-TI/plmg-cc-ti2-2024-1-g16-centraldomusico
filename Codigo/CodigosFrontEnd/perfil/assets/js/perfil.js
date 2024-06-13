document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    const id = localStorage.getItem('id');
    const token = localStorage.getItem('token');

    if (token) {
        document.getElementById('loginbotao').style.display = 'none';
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
                    localStorage.setItem('nome', data.nome);
                    document.getElementById('descricao').textContent = data.descricao;
                    document.getElementById('cache').textContent = formatCurrency(data.cache);
                    document.getElementById('instrumento1').textContent = data.instrumento1;
                    document.getElementById('instrumento2').textContent = data.instrumento2;
                    document.getElementById('instrumento3').textContent = data.instrumento3;
                    document.getElementById('objetivo').textContent = data.objetivo;
                    document.getElementById('estilo').textContent = data.estilo;
                    document.getElementById('banda').textContent = data.bandaNome;
                    localStorage.setItem('bandaId', data.bandaId);

                    // Verificar e exibir a imagem de perfil
                    if (data.profileImage) {
                        const profileImageElem = document.getElementById('fotoPerfil');
                        profileImageElem.src = `data:image/png;base64,${data.profileImage}`;
                        profileImageElem.style.display = 'block';
                    }

                    // Se o usuário não tiver banda, mostrar "Sem banda"
                    if (!data.bandaNome) {
                        document.getElementById('banda').textContent = 'Sem banda';
                    }
                }
            })
            .catch(error => {
                console.error('Erro ao buscar dados do usuário:', error);
                document.getElementById('msgError').textContent = 'Erro ao carregar os dados do usuário. Tente novamente.';
            });
    } else {
        window.location.href = '/Codigo/CodigosFrontEnd/Login/novologin.html';
    }
});

//criar funcao onclick para logout
function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('id');
    localStorage.removeItem('secret');
    window.location.href = '/Codigo/CodigosFrontEnd/Login/novologin.html';
}

function formatCurrency(value) {
    value = value.toFixed(2).replace('.', ',');
    return `R$ ${value}`;
}

const cacheDisplay = document.getElementById('cache');
cacheDisplay.textContent = formatCurrency(document.getElementById('cache'));

function editarPerfil() {
    //tornar campos editaveis
    document.getElementById('nome').contentEditable = true;
    document.getElementById('descricao').contentEditable = true;
    document.getElementById('cache').contentEditable = true;
    document.getElementById('instrumento1').contentEditable = true;
    document.getElementById('instrumento2').contentEditable = true;
    document.getElementById('instrumento3').contentEditable = true;
    document.getElementById('objetivo').contentEditable = true;
    document.getElementById('estilo').contentEditable = true;
    //mostrar botao de salvar
    document.getElementById('salvar').style.display = 'block';
    //esconder botao de editar
    document.getElementById('editar').style.display = 'none';
    document.getElementById('fotoPerfil').style.display = 'block';
}
function salvar() {
    
    const nome = document.getElementById('nome').textContent.trim();
    const descricao = document.getElementById('descricao').textContent.trim();
    const cache = parseFloat(document.getElementById('cache').textContent.trim().replace(',', '.'));
    const instrumento1 = document.getElementById('instrumento1').textContent.trim();
    const instrumento2 = document.getElementById('instrumento2').textContent.trim();
    const instrumento3 = document.getElementById('instrumento3').textContent.trim();
    const objetivo = document.getElementById('objetivo').textContent.trim();
    const estilo = document.getElementById('estilo').textContent.trim();
    const id = localStorage.getItem('id');
    const senha = localStorage.getItem('secret');
    const token = localStorage.getItem('token');
    var fotoPerfil = document.getElementById('inputFotoPerfil').files[0]; // Obter o arquivo de imagem

    console.log('senha:', senha);
    const perfil = {
        nome: nome,
        descricao: descricao,
        senha: senha,
        cache: cache,
        instrumento1: instrumento1,
        instrumento2: instrumento2,
        instrumento3: instrumento3,
        objetivo: objetivo,
        estilo: estilo,
        fotoPerfil: fotoPerfil
    };
    
    fetch(`http://localhost:6789/usuario/update?id=${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(perfil)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro na requisição: ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        console.log('Resposta do servidor:', data);
        alert('Perfil atualizado com sucesso!');
        window.location.reload();
    })
    .catch(error => {
        console.error('Erro durante a requisição fetch:', error);
        alert('Erro ao atualizar o perfil. Tente novamente.');
    });

    // Tornar campos não editáveis
    document.getElementById('nome').contentEditable = false;
    document.getElementById('descricao').contentEditable = false;
    document.getElementById('cache').contentEditable = false;
    document.getElementById('instrumento1').contentEditable = false;
    document.getElementById('instrumento2').contentEditable = false;
    document.getElementById('instrumento3').contentEditable = false;
    document.getElementById('objetivo').contentEditable = false;
    document.getElementById('estilo').contentEditable = false;
    
    // Mostrar botão de editar
    document.getElementById('editar').style.display = 'block';
    // Esconder botão de salvar
    document.getElementById('salvar').style.display = 'none';
    document.getElementById('fotoPerfil').style.display = 'none';
}
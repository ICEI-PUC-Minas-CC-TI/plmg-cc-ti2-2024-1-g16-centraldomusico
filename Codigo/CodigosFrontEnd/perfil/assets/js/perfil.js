document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    const id = localStorage.getItem('id');
    const token = localStorage.getItem('token');
    const user = JSON.parse(localStorage.getItem('usuario'));
    if (token) {
        document.getElementById('loginbotao').style.display = 'none';
        loadUserProfile(id, token);
    } else {
        window.location.href = '/Codigo/CodigosFrontEnd/Login/novologin.html';
    }
});

function loadUserProfile(id, token) {
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
            updateUserProfileUI(data);
            localStorage.setItem('nome', data.nome);
            localStorage.setItem('bandaId', data.bandaId);

            // Definir o valor do inputFotoPerfil para a foto de perfil atual, se existir
            if (data.profileImage) {
                const profileImageElem = document.getElementById('fotoPerfil');
                profileImageElem.src = `data:image/png;base64,${data.profileImage}`;
                profileImageElem.style.display = 'block';
                document.getElementById('inputFotoPerfil').src = profileImageElem.src;
            }
            //salvar objeto no local storage
            localStorage.setItem('usuario', JSON.stringify(data));
            //printar objeto do local storage
            console.log(JSON.parse(localStorage.getItem('usuario')));


        }
    })
    .catch(error => {
        console.error('Erro ao buscar dados do usuário:', error);
        document.getElementById('msgError').textContent = 'Erro ao carregar os dados do usuário. Tente novamente.';
    });
}

function updateUserProfileUI(data) {
    document.getElementById('nome').textContent = data.nome;
    document.getElementById('descricao').textContent = data.descricao;
    document.getElementById('cache').textContent = formatCurrency(data.cache);
    document.getElementById('instrumento1').textContent = data.instrumento1;
    document.getElementById('instrumento2').textContent = data.instrumento2;
    document.getElementById('instrumento3').textContent = data.instrumento3;
    document.getElementById('objetivo').textContent = data.objetivo;
    document.getElementById('estilo').textContent = data.estilo;
    document.getElementById('banda').textContent = data.bandaNome || 'Sem banda';
    // colocar banda no local storage

    if (data.profileImage) {
        const profileImageElem = document.getElementById('fotoPerfil');
        profileImageElem.src = `data:image/png;base64,${data.profileImage}`;
        profileImageElem.style.display = 'block';
    }
}

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('id');
    localStorage.removeItem('secret');
    localStorage.removeItem('usuario');
    window.location.href = '/Codigo/CodigosFrontEnd/Login/novologin.html';
}

function formatCurrency(value) {
    return `R$ ${value.toFixed(2).replace('.', ',')}`;
}

const cacheDisplay = document.getElementById('cache');
cacheDisplay.textContent = formatCurrency(parseFloat(cacheDisplay.textContent.replace('R$', '').replace('.', '').replace(',', '.')));

function editarPerfil() {
    document.getElementById('nome').contentEditable = true;
    document.getElementById('descricao').contentEditable = true;
    document.getElementById('cache').contentEditable = true;
    document.getElementById('instrumento1').contentEditable = true;
    document.getElementById('instrumento2').contentEditable = true;
    document.getElementById('instrumento3').contentEditable = true;
    document.getElementById('objetivo').contentEditable = true;
    document.getElementById('estilo').contentEditable = true;
    document.getElementById('salvar').style.display = 'block';
    document.getElementById('editar').style.display = 'none';
    document.getElementById('fotoPerfil').style.display = 'block';
    document.getElementById('inputFotoPerfil').style.display = 'block';
}

function salvar() {
    const nome = document.getElementById('nome').textContent.trim();
    const descricao = document.getElementById('descricao').textContent.trim();
    const cache = parseFloat(document.getElementById('cache').textContent.replace('R$', '').replace('.', '').replace(',', '.'));
    const instrumento1 = document.getElementById('instrumento1').textContent.trim();
    const instrumento2 = document.getElementById('instrumento2').textContent.trim();
    const instrumento3 = document.getElementById('instrumento3').textContent.trim();
    const objetivo = document.getElementById('objetivo').textContent.trim();
    const estilo = document.getElementById('estilo').textContent.trim();
    const id = localStorage.getItem('id');
    const senha = localStorage.getItem('secret');
    const token = localStorage.getItem('token');
    const fotoPerfil = document.getElementById('inputFotoPerfil').files[0];

    const formData = new FormData();
    formData.append('id', id);
    formData.append('nome', nome);
    formData.append('descricao', descricao);
    formData.append('senha', senha);
    formData.append('cache', cache);
    formData.append('instrumento1', instrumento1);
    formData.append('instrumento2', instrumento2);
    formData.append('instrumento3', instrumento3);
    formData.append('objetivo', objetivo);
    formData.append('estilo', estilo);

    if (fotoPerfil) {
        formData.append('fotoPerfil', fotoPerfil);
    }

    fetch(`http://localhost:6789/usuario/update`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`
        },
        body: formData
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

    document.getElementById('nome').contentEditable = false;
    document.getElementById('descricao').contentEditable = false;
    document.getElementById('cache').contentEditable = false;
    document.getElementById('instrumento1').contentEditable = false;
    document.getElementById('instrumento2').contentEditable = false;
    document.getElementById('instrumento3').contentEditable = false;
    document.getElementById('objetivo').contentEditable = false;
    document.getElementById('estilo').contentEditable = false;

    document.getElementById('editar').style.display = 'block';
    document.getElementById('salvar').style.display = 'none';
    document.getElementById('fotoPerfil').style.display = 'none';
    document.getElementById('inputFotoPerfil').style.display = 'none';

    // Atualizar o localStorage apenas após a leitura completa da imagem
    if (fotoPerfil) {
        const reader = new FileReader();
        reader.onload = function(e) {
            const usuario = JSON.parse(localStorage.getItem('usuario'));
            usuario.nome = nome;
            usuario.descricao = descricao;
            usuario.cache = cache;
            usuario.instrumento1 = instrumento1;
            usuario.instrumento2 = instrumento2;
            usuario.instrumento3 = instrumento3;
            usuario.objetivo = objetivo;
            usuario.estilo = estilo;
            usuario.profileImage = e.target.result; // Armazenar a imagem como URL de dados

            localStorage.setItem('usuario', JSON.stringify(usuario));
        }
        reader.readAsDataURL(fotoPerfil); // Inicia a leitura da imagem
    } else {
        // Se não houver nova foto, apenas atualize as outras informações do usuário no localStorage
        const usuario = JSON.parse(localStorage.getItem('usuario'));
        usuario.nome = nome;
        usuario.descricao = descricao;
        usuario.cache = cache;
        usuario.instrumento1 = instrumento1;
        usuario.instrumento2 = instrumento2;
        usuario.instrumento3 = instrumento3;
        usuario.objetivo = objetivo;
        usuario.estilo = estilo;

        localStorage.setItem('usuario', JSON.stringify(usuario));
    }
}
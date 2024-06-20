document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    //pegar o id que ta no parametro da url
    const id = urlParams.get('id');
    //pegar instrumento1, instrumento2 e instrumento3 nos parametros da url
    const ins1 = urlParams.get('instrumento1').toLowerCase();
    const ins2 = urlParams.get('instrumento2').toLowerCase();
    const ins3 = urlParams.get('instrumento3').toLowerCase();
    console.log("instrumento1: "+instrumento1);
    loadUserProfile(id);
    loadInstrumentos(id,ins1,ins2,ins3);
});

//funcao para pegar do backend o nome dos instrumentos do usuario, caso esses nomes batam com os nomes dos instrumentos que ele colocou no frontend, printar Usuario Verificado
function loadInstrumentos(id,ins1,ins2,ins3) {
    fetch(`http://localhost:6789/usuario/getNomeInstrumentos?id=${id}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
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
        if (data && data.instrumentos) {
            const instrumentosUsuario = JSON.parse(data.instrumentos); // Convertendo a string JSON para array JavaScript
            var instrumento1 = ins1;
            var instrumento2 = ins2;
            var instrumento3 = ins3;

            console.log("INSTRUMENTO1");
            console.log(instrumento1);
            console.log("INSTRUMENTO3");
            console.log(instrumento3);
            console.log("SEPARADOR");
            console.log(instrumento1, instrumento2, instrumento3);
            console.log("SEPARADOR");
            //deixar todos os instrumentos em minusculo
            instrumentosUsuario.forEach((inst, index) => {
                instrumentosUsuario[index] = inst.toLowerCase();
            });
            console.log(instrumentosUsuario);

            console.log("SEPARADOR");
            // Criando um array com os instrumentos do usuário
            const verificaInstrumentos = [instrumento1, instrumento2, instrumento3];
            console.log(verificaInstrumentos);
            console.log("SEPARADOR");
            // Verificando se todos os instrumentos do usuário estão presentes na lista recebida
            const usuarioVerificado = verificaInstrumentos.every(inst => instrumentosUsuario.includes(inst));
    
            // Criando o card com base na verificação
            const card = document.createElement('div');
            card.classList.add('card');
            //colocar badge de usuario verificado
            card.innerHTML = `
                <div class="card-body">
                    <img src="${usuarioVerificado ? '/Codigo/CodigosFrontEnd/perfil/assets/img/verified.png' : '/Codigo/CodigosFrontEnd/perfil/assets/img/not-verified.png'}" class="card-img-top" id="imgver" alt="Verificado">
                </div>
            `;
    
            document.getElementById('verificado').appendChild(card);
        }
    })
    .catch(error => {
        console.error('Erro ao processar requisição:', error);
    });
}



function loadUserProfile(id) {
    fetch(`http://localhost:6789/usuario/get/perfil?id=${id}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
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

            // Definir o valor do inputFotoPerfil para a foto de perfil atual, se existir
            if (data.profileImage) {
                const profileImageElem = document.getElementById('fotoPerfil');
                profileImageElem.src = `data:image/png;base64,${data.profileImage}`;
                //colocar classe
                profileImageElem.classList.add('fotodeperfil');
                profileImageElem.style.display = 'block';
                document.getElementById('inputFotoPerfil').src = profileImageElem.src;
            }
            //salvar objeto no local storage
            console.log(data);
            console.log("Telefone: "+data.telefone);
            //printar objeto do local storage


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
    //printar a data
    document.getElementById('telefone').textContent = data.telefone;
    document.getElementById('banda').textContent = data.bandaNome || 'Sem banda';
    // colocar banda no local storage

    if (data.profileImage) {
        const profileImageElem = document.getElementById('fotoPerfil');
        profileImageElem.src = `data:image/png;base64,${data.profileImage}`;
        profileImageElem.style.display = 'block';
    }
}



function formatCurrency(value) {
    return `R$ ${value.toFixed(2).replace('.', ',')}`;
}

const cacheDisplay = document.getElementById('cache');
cacheDisplay.textContent = formatCurrency(parseFloat(cacheDisplay.textContent.replace('R$', '').replace('.', '').replace(',', '.')));


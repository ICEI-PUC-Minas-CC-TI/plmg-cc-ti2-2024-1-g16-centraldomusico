
document.addEventListener('DOMContentLoaded', function () {
    const urlParams = new URLSearchParams(window.location.search);
    Id = urlParams.get('id');
    

    const paginaperfil = document.getElementById('perfilCard');

    fetch(`https://jsonservercentraldomusico.arthurcarvalh19.repl.co/perfil/${Id}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Erro ao carregar detalhes do álbum: ${response.status}`);
                }
                return response.json();
            })
            .then(card => {


                // Exibir os detalhes do álbum no DOM
                paginaperfil.innerHTML = `
                <img id="profileP" src="https://thispersondoesnotexist.com">

                <h2>NOME:</h2>
                <h3 class="nome">${card.nome}</h3>
        
                <h2>Descrição:</h2>
                <h3 class="descricao">${card.descricao}</h3>
        
                <h2>Instrumentos:</h2>
                <h3 class="instrumentos"> ${card.instrumento1}, ${card.instrumento2}, ${card.instrumento3} </h3>
        
                <h2>Redes sociais e contato</h2>
                <ul class="contatos">
                    <a href="#"><li class="insta">${card.instagram}</li></a>
                    <li class="ctt">${card.telefone}</li>
                </ul>`;
            })
     })
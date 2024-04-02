$(document).ready(function () {
    // Inicialize o botão de colapso de barra lateral
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
    });
    // Chame a função carregarJSON depois que o DOM estiver pronto
    carregarJSON();
});



function carregarJSON() {
    // Requisição para carregar o arquivo JSON usando fetch
    fetch('https://jsonservercentraldomusico.arthurcarvalh19.repl.co/anuncio')
        .then(response => {
            if (!response.ok) {
                throw new Error(`Erro na solicitação: ${response.status} ${response.statusText}`);
            }
            return response.json(); // Converte a resposta para JSON
        })
        .then(dados => {
            // Exibir os dados no HTML
            var listaLocais = document.getElementById('listaLocais');
            dados.forEach(function (local) {
                var listItem = document.createElement('li');
                listItem.innerHTML = `<strong>Nome:</strong> ${local.casaEvento} <br> <strong> Descrição: </strong> ${local.descricao}<br> <strong> Agenda: </strong> ${local.data} `;
                listaLocais.appendChild(listItem);
            });
        })
        .catch(error => {
            console.error('Erro ao carregar JSON:', error);
        });
}

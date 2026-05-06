const BASE_URL = "http://localhost:8080";

document.addEventListener("DOMContentLoaded", () => {
  document
    .getElementById("formProjeto")
    .addEventListener("submit", adicionarProjeto);
  document
    .getElementById("formVinculo")
    .addEventListener("submit", vincularFuncionario);
  document
    .getElementById("formSetor")
    .addEventListener("submit", adicionarSetor);
  document
    .getElementById("formFuncionario")
    .addEventListener("submit", adicionarFuncionario);
  carregarProjetos();
  carregarSetores();
  carregarFuncionarios();
});

async function requestJson(url, options = {}) {
  const response = await fetch(url, {
    headers: {
      "Content-Type": "application/json",
      ...(options.headers || {}),
    },
    ...options,
  });

  if (!response.ok) {
    let message = `Erro ao chamar ${url}`;
    try {
      const errorBody = await response.json();
      message = errorBody.message || errorBody.error || message;
    } catch (error) {
      const text = await response.text();
      if (text) {
        message = text;
      }
    }
    throw new Error(message);
  }

  if (response.status === 204) {
    return null;
  }

  const contentType = response.headers.get("content-type") || "";
  if (!contentType.includes("application/json")) {
    return null;
  }

  return response.json();
}

function formatarData(valor) {
  return valor ? new Date(valor).toLocaleDateString("pt-BR") : "-";
}

function mostrarMensagem(elementId, texto, tipo = "success") {
  const elemento = document.getElementById(elementId);
  elemento.textContent = texto;
  elemento.className = `feedback ${tipo}`;
}

function limparMensagem(elementId) {
  const elemento = document.getElementById(elementId);
  elemento.textContent = "";
  elemento.className = "feedback";
}

function preencherSelect(elemento, itens, valorVazio) {
  elemento.innerHTML = "";
  if (valorVazio) {
    const option = document.createElement("option");
    option.value = "";
    option.textContent = valorVazio;
    elemento.appendChild(option);
  }

  itens.forEach((item) => {
    const option = document.createElement("option");
    option.value = item.id;
    option.textContent = `${item.id} - ${item.nome || item.descricao}`;
    elemento.appendChild(option);
  });
}

async function carregarProjetos() {
  const listaProjetos = document.getElementById("listaProjetos");
  const projetoSelect = document.getElementById("projetoVinculo");

  try {
    const projetos = await requestJson(`${BASE_URL}/projetos`);

    if (!projetos || projetos.length === 0) {
      listaProjetos.innerHTML =
        '<div class="empty-state">Nenhum projeto cadastrado.</div>';
      projetoSelect.innerHTML =
        '<option value="">Nenhum projeto disponível</option>';
      return;
    }

    listaProjetos.innerHTML = projetos
      .map(
        (projeto) => `
            <article class="projeto-card">
                <div class="card-topline">
                    <span class="badge">Projeto #${projeto.id}</span>
                </div>
                <h3>${projeto.descricao}</h3>
                <p><strong>Início:</strong> ${formatarData(projeto.dataInicio)}</p>
                <p><strong>Fim:</strong> ${formatarData(projeto.dataFim)}</p>
                <p><strong>Funcionários:</strong> ${projeto.funcionarios && projeto.funcionarios.length ? projeto.funcionarios.map((funcionario) => funcionario.nome).join(", ") : "Nenhum vinculado"}</p>
            </article>
        `,
      )
      .join("");

    preencherSelect(projetoSelect, projetos, "Selecione um projeto");
  } catch (error) {
    listaProjetos.innerHTML = `<div class="empty-state error">${error.message}</div>`;
  }
}

async function carregarSetores() {
  const listaSetores = document.getElementById("listaSetores");
  const setorSelect = document.getElementById("setorFuncionario");

  try {
    const setores = await requestJson(`${BASE_URL}/setores`);

    if (!setores || setores.length === 0) {
      listaSetores.innerHTML =
        '<div class="empty-state">Nenhum setor cadastrado.</div>';
      setorSelect.innerHTML = '<option value="">Sem setor</option>';
      return;
    }

    listaSetores.innerHTML = setores
      .map(
        (setor) => `
            <article class="mini-card">
                <h3>${setor.nome}</h3>
                <p><strong>ID:</strong> ${setor.id}</p>
                <p><strong>Funcionários:</strong> ${setor.funcionarios && setor.funcionarios.length ? setor.funcionarios.length : 0}</p>
            </article>
        `,
      )
      .join("");

    preencherSelect(setorSelect, setores, "Sem setor");
  } catch (error) {
    listaSetores.innerHTML = `<div class="empty-state error">${error.message}</div>`;
  }
}

async function carregarFuncionarios() {
  const listaFuncionarios = document.getElementById("listaFuncionarios");
  const funcionarioSelect = document.getElementById("funcionarioVinculo");

  try {
    const funcionarios = await requestJson(`${BASE_URL}/funcionarios`);

    if (!funcionarios || funcionarios.length === 0) {
      listaFuncionarios.innerHTML =
        '<div class="empty-state">Nenhum funcionário cadastrado.</div>';
      funcionarioSelect.innerHTML =
        '<option value="">Nenhum funcionário disponível</option>';
      return;
    }

    listaFuncionarios.innerHTML = funcionarios
      .map(
        (funcionario) => `
            <article class="mini-card">
                <h3>${funcionario.nome}</h3>
                <p><strong>ID:</strong> ${funcionario.id}</p>
            </article>
        `,
      )
      .join("");

    preencherSelect(
      funcionarioSelect,
      funcionarios,
      "Selecione um funcionário",
    );
  } catch (error) {
    listaFuncionarios.innerHTML = `<div class="empty-state error">${error.message}</div>`;
  }
}

async function adicionarProjeto(event) {
  event.preventDefault();
  limparMensagem("mensagemProjeto");

  const descricao = document.getElementById("descricao").value.trim();
  const dataInicio = document.getElementById("dataInicio").value;
  const dataFim = document.getElementById("dataFim").value;

  if (!descricao || !dataInicio || !dataFim) {
    mostrarMensagem(
      "mensagemProjeto",
      "Preencha todos os campos do projeto.",
      "error",
    );
    return;
  }

  if (new Date(dataInicio) > new Date(dataFim)) {
    mostrarMensagem(
      "mensagemProjeto",
      "A data de início não pode ser maior que a data final.",
      "error",
    );
    return;
  }

  try {
    await requestJson(`${BASE_URL}/projetos`, {
      method: "POST",
      body: JSON.stringify({ descricao, dataInicio, dataFim }),
    });

    document.getElementById("formProjeto").reset();
    mostrarMensagem("mensagemProjeto", "Projeto cadastrado com sucesso.");
    await carregarProjetos();
  } catch (error) {
    mostrarMensagem("mensagemProjeto", error.message, "error");
  }
}

async function adicionarSetor(event) {
  event.preventDefault();
  limparMensagem("mensagemSetor");

  const nome = document.getElementById("nomeSetor").value.trim();

  if (!nome) {
    mostrarMensagem("mensagemSetor", "Informe o nome do setor.", "error");
    return;
  }

  try {
    await requestJson(`${BASE_URL}/setores`, {
      method: "POST",
      body: JSON.stringify({ nome }),
    });

    document.getElementById("formSetor").reset();
    mostrarMensagem("mensagemSetor", "Setor cadastrado com sucesso.");
    await carregarSetores();
    await carregarFuncionarios();
  } catch (error) {
    mostrarMensagem("mensagemSetor", error.message, "error");
  }
}

async function adicionarFuncionario(event) {
  event.preventDefault();
  limparMensagem("mensagemFuncionario");

  const nome = document.getElementById("nomeFuncionario").value.trim();
  const setorIdRaw = document.getElementById("setorFuncionario").value;
  const setorId = setorIdRaw ? Number(setorIdRaw) : null;

  if (!nome) {
    mostrarMensagem(
      "mensagemFuncionario",
      "Informe o nome do funcionário.",
      "error",
    );
    return;
  }

  try {
    await requestJson(`${BASE_URL}/funcionarios`, {
      method: "POST",
      body: JSON.stringify({ nome, setorId }),
    });

    document.getElementById("formFuncionario").reset();
    mostrarMensagem(
      "mensagemFuncionario",
      "Funcionário cadastrado com sucesso.",
    );
    await carregarFuncionarios();
  } catch (error) {
    mostrarMensagem("mensagemFuncionario", error.message, "error");
  }
}

async function vincularFuncionario(event) {
  event.preventDefault();
  limparMensagem("mensagemVinculo");

  const idProjeto = document.getElementById("projetoVinculo").value;
  const idFuncionario = document.getElementById("funcionarioVinculo").value;

  if (!idProjeto || !idFuncionario) {
    mostrarMensagem(
      "mensagemVinculo",
      "Selecione um projeto e um funcionário.",
      "error",
    );
    return;
  }

  try {
    await requestJson(
      `${BASE_URL}/projetos/${idProjeto}/funcionarios/${idFuncionario}`,
      {
        method: "POST",
      },
    );

    mostrarMensagem(
      "mensagemVinculo",
      "Funcionário vinculado ao projeto com sucesso.",
    );
    await carregarProjetos();
  } catch (error) {
    mostrarMensagem("mensagemVinculo", error.message, "error");
  }
}

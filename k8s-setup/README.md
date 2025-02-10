# Kubernetes Cluster Setup

Este repositório contém os arquivos necessários para configurar um cluster Kubernetes que:
- Recebe arquivos grandes via aplicação web
- Armazena os arquivos em um volume persistente
- Processa os arquivos usando um serviço CLI

## Arquivos

- `persistent_volume.yaml`: Define o volume persistente e o claim.
- `deployment_app_web.yaml`: Configuração da aplicação web.
- `job_processador.yaml`: Job que processa os arquivos.

## Como aplicar no Kubernetes

```sh
kubectl apply -f persistent_volume.yaml
kubectl apply -f deployment_app_web.yaml
kubectl apply -f job_processador.yaml
```

Certifique-se de configurar corretamente o **NFS ou outro storage** para o volume persistente.

FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
WORKDIR /src
COPY ["BackendDotnet.csproj", "./"]
RUN dotnet restore
COPY . .
RUN dotnet publish -c Release -o /app/publish

FROM mcr.microsoft.com/dotnet/aspnet:8.0
WORKDIR /app
COPY --from=build /app/publish .
# 포트 3333 설정
ENV ASPNETCORE_URLS=http://+:3333
EXPOSE 3333
ENTRYPOINT ["dotnet", "BackendDotnet.dll"]
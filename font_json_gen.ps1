Get-ChildItem -Filter "*.ttf" | ForEach-Object {
    $jsonPath = "$($_.BaseName).json"

    if (Test-Path $jsonPath) {
        Write-Host "Skipping $jsonPath (already exists)"
        return
    }

    $json = @"
{
  "providers": [
    {
      "type": "ttf",
      "file": "footlib:$($_.Name)",
      "size": 12,
      "oversample": 2
    }
  ]
}
"@

    Set-Content -Path $jsonPath -Value $json -Encoding UTF8
    Write-Host "Created $jsonPath"
}

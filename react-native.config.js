module.exports = {
  dependency: {
    platforms: {
      android: {
        sourceDir: './android',
        packageImportPath: 'import one.telefon.tele.TeleModulePackage;',
        packageInstance: 'new TeleModulePackage()',
      },
    },
  },
}; 
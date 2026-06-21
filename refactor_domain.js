const fs = require('fs');
const path = require('path');

const basePath = 'f:/JAVABACKENDYoot/Demo2/LeLa/src/main/java/com/lela';
const entityPath = path.join(basePath, 'domain', 'entity');
const enumPath = path.join(basePath, 'domain', 'enums');

const entityFiles = fs.existsSync(entityPath) ? fs.readdirSync(entityPath).filter(f => f.endsWith('.java')) : [];
const enumFiles = fs.existsSync(enumPath) ? fs.readdirSync(enumPath).filter(f => f.endsWith('.java')) : [];

const moves = []; 

entityFiles.forEach(file => {
    const className = file.replace('.java', '');
    const newPackage = `com.lela.${className}.domain`;
    moves.push({
        oldPath: path.join(entityPath, file),
        newPath: path.join(basePath, className, 'domain', file),
        oldImport: `com.lela.domain.entity.${className}`,
        newImport: `${newPackage}.${className}`,
        newPackageStr: newPackage,
        className: className,
        isEnum: false
    });
});

enumFiles.forEach(file => {
    const className = file.replace('.java', '');
    const newPackage = `com.lela.${className}.domain`;
    moves.push({
        oldPath: path.join(enumPath, file),
        newPath: path.join(basePath, className, 'domain', file),
        oldImport: `com.lela.domain.enums.${className}`,
        newImport: `${newPackage}.${className}`,
        newPackageStr: newPackage,
        className: className,
        isEnum: true
    });
});

function getAllJavaFiles(dir) {
    let results = [];
    const list = fs.readdirSync(dir);
    list.forEach(file => {
        file = path.join(dir, file);
        const stat = fs.statSync(file);
        if (stat && stat.isDirectory()) {
            results = results.concat(getAllJavaFiles(file));
        } else if (file.endsWith('.java')) {
            results.push(file);
        }
    });
    return results;
}

const allJavaFiles = getAllJavaFiles(basePath);

// Update all files imports FIRST before we change their packages, or it doesn't matter since we process in memory
allJavaFiles.forEach(file => {
    let content = fs.readFileSync(file, 'utf8');
    let originalContent = content;
    
    const pkgMatch = content.match(/package\s+([a-zA-Z0-9_.]+)\s*;/);
    const thisPackage = pkgMatch ? pkgMatch[1] : '';

    // Replace explicit old imports
    moves.forEach(m => {
        const importRegex = new RegExp(`import\\s+${m.oldImport.replace(/\\./g, '\\\\.')}\\s*;`, 'g');
        content = content.replace(importRegex, `import ${m.newImport};`);
    });
    
    // Inject implicit imports
    let missingImports = new Set();
    moves.forEach(m => {
        // Skip if this file is the class itself being moved
        if (file === m.oldPath || file === m.newPath) return;
        
        const classUsageRegex = new RegExp(`\\b${m.className}\\b`);
        if (classUsageRegex.test(content)) {
            const hasImportRegex = new RegExp(`import\\s+${m.newImport.replace(/\\./g, '\\\\.')}\\s*;`);
            // Check if not already imported and not in the same package
            if (!hasImportRegex.test(content) && thisPackage !== m.newPackageStr) {
                missingImports.add(`import ${m.newImport};`);
            }
        }
    });

    if (missingImports.size > 0) {
        const importsStr = Array.from(missingImports).join('\n') + '\n';
        const lastImportIndex = content.lastIndexOf('import ');
        if (lastImportIndex !== -1) {
            const endOfLastImport = content.indexOf(';', lastImportIndex) + 1;
            content = content.substring(0, endOfLastImport) + '\n' + importsStr + content.substring(endOfLastImport);
        } else if (pkgMatch) {
            const endOfPkg = content.indexOf(';', pkgMatch.index) + 1;
            content = content.substring(0, endOfPkg) + "\n\n" + importsStr + content.substring(endOfPkg);
        }
    }

    if (content !== originalContent) {
        fs.writeFileSync(file, content);
    }
});

// Now move and update packages for the domain files themselves
moves.forEach(m => {
    if (!fs.existsSync(path.dirname(m.newPath))) {
        fs.mkdirSync(path.dirname(m.newPath), { recursive: true });
    }
    
    if (fs.existsSync(m.oldPath)) {
        let content = fs.readFileSync(m.oldPath, 'utf8');
        content = content.replace(/package\s+com\.lela\.domain\.(entity|enums)\s*;/, `package ${m.newPackageStr};`);
        fs.writeFileSync(m.newPath, content);
        fs.unlinkSync(m.oldPath);
    }
});

console.log("Refactoring complete.");

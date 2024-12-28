document.addEventListener("DOMContentLoaded", function() {
    const healthContent = document.getElementById("healthContent");
    const infoContent = document.getElementById("infoContent");
    const flywayContent = document.getElementById("flywayContent");
    const readyTimeContent = document.getElementById("readyTimeContent");
    const jvmMemoryUsedContent = document.getElementById("jvmMemoryUsedContent");
    const cpuUsageContent = document.getElementById("cpuUsageContent");

    fetchHealth();
    fetchInfo();
    fetchFlyway();
    fetchMetric("application.ready.time", readyTimeContent);
    fetchMetric("jvm.memory.used", jvmMemoryUsedContent);
    fetchMetric("system.cpu.usage", cpuUsageContent);

    function fetchHealth() {
        fetch("/actuator/health")
            .then(r => r.json())
            .then(data => {
                healthContent.textContent = parseHealth(data);
            })
            .catch(err => {
                console.error("Error fetching health:", err);
                healthContent.textContent = msgErrorFetchingHealth;
            });
    }
    function parseHealth(data) {
        let status = data.status || msgStatusUnknown;
        return `Status: ${status}`;
    }

    function fetchInfo() {
        fetch("/actuator/info")
            .then(r => r.json())
            .then(data => {
                infoContent.textContent = parseInfo(data);
            })
            .catch(err => {
                console.error("Error fetching info:", err);
                infoContent.textContent = msgErrorFetchingInfo;
            });
    }
    function parseInfo(data) {
        if (!data.build) return msgNoBuildInfo;
        let b = data.build;
        return `Build Artifact: ${b.artifact}
Name: ${b.name}
Group: ${b.group}
Version: ${b.version}
Time: ${b.time}`;
    }

    function fetchFlyway() {
        fetch("/actuator/flyway")
            .then(r => r.json())
            .then(data => {
                flywayContent.textContent = parseFlyway(data);
            })
            .catch(err => {
                console.error("Error fetching flyway:", err);
                flywayContent.textContent = msgErrorFetchingFlyway;
            });
    }
    function parseFlyway(data) {
        let result = [];
        try {
            let contexts = data.contexts || {};
            let contextKeys = Object.keys(contexts);
            contextKeys.forEach(ctxKey => {
                let flywayBeans = contexts[ctxKey].flywayBeans || {};
                Object.keys(flywayBeans).forEach(beanKey => {
                    let bean = flywayBeans[beanKey];
                    let migrations = bean.migrations || [];
                    migrations.forEach(m => {
                        let line = `Version: ${m.version}, Desc: ${m.description}, State: ${m.state}, InstalledOn: ${m.installedOn}`;
                        result.push(line);
                    });
                });
            });
        } catch (e) {
            console.error("Flyway parse error:", e);
            return msgFlywayParseError;
        }

        if (result.length === 0) {
            return msgNoMigrationsFound;
        }
        return result.join("\n");
    }

    function fetchMetric(metricName, element) {
        let url = `/actuator/metrics/${metricName}`;
        fetch(url)
            .then(r => r.json())
            .then(data => {
                element.textContent = parseMetric(data);
            })
            .catch(err => {
                console.error("Error fetching metric:", metricName, err);
                element.textContent = msgErrorFetchingMetric + metricName;
            });
    }

    function parseMetric(data) {
        let name = data.name || msgUnknownMetric;
        let desc = data.description || "";
        let measurements = data.measurements || [];
        let value = measurements[0] ? measurements[0].value : msgMetricNa;

        return `Metric Name: ${name}
Description: ${desc}
Value: ${value}`;
    }
});

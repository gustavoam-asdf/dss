package eu.europa.esig.dss.validation.job;

import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.Digest;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.validation.job.cache.CacheKey;
import eu.europa.esig.dss.validation.job.cache.DownloadCache;
import eu.europa.esig.dss.validation.job.cache.ParsingCache;
import eu.europa.esig.dss.validation.job.cache.ValidationCache;
import eu.europa.esig.dss.validation.job.cache.access.AbstractCacheAccessByKey;
import eu.europa.esig.dss.validation.job.cache.access.CacheAccessByKey;
import eu.europa.esig.dss.validation.job.cache.access.ParametrizedReadOnlyCacheAccess;
import eu.europa.esig.dss.validation.job.download.DownloadResult;
import eu.europa.esig.dss.validation.job.dto.AbstractParsingCacheDTO;
import eu.europa.esig.dss.validation.job.dto.DownloadCacheDTO;
import eu.europa.esig.dss.validation.job.dto.ValidationCacheDTO;
import eu.europa.esig.dss.validation.job.parsing.AbstractParsingResult;
import eu.europa.esig.dss.validation.job.parsing.ParsingResult;
import eu.europa.esig.dss.validation.job.validation.ValidationResult;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Contains methods to create mock implementations
 *
 */
public abstract class AbstractTestValidationJob {

    protected DownloadResult getDownloadResult(DSSDocument dssDocument, Digest digest) {
        return new DownloadResult() {
            @Override
            public DSSDocument getDSSDocument() {
                return dssDocument;
            }
            @Override
            public Digest getDigest() {
                return digest;
            }
            @Override
            public List<String> getSha2ErrorMessages() {
                return null;
            }
        };
    }

    protected ParsingResult getParsingResult() {
        return new AbstractParsingResult() {};
    }

    protected ValidationResult getValidationResult(Indication indication) {
        return new ValidationResult() {
            @Override
            public Indication getIndication() {
                return indication;
            }
            @Override
            public SubIndication getSubIndication() {
                return null;
            }
            @Override
            public Date getSigningTime() {
                return null;
            }
            @Override
            public CertificateToken getSigningCertificate() {
                return null;
            }
            @Override
            public List<CertificateToken> getPotentialSigners() {
                return null;
            }
        };
    }

    protected CacheAccessByKey getCacheAccessByKey(CacheKey cacheKey, DownloadCache downloadCache,
                                                   ParsingCache parsingCache, ValidationCache validationCache) {
        return new AbstractCacheAccessByKey<DownloadCacheDTO, AbstractParsingCacheDTO, ValidationCacheDTO>(
                cacheKey, downloadCache, parsingCache, validationCache, getParametrizedReadOnlyCacheAccess()) {
        };
    }

    protected ParametrizedReadOnlyCacheAccess<DownloadCacheDTO, AbstractParsingCacheDTO, ValidationCacheDTO> getParametrizedReadOnlyCacheAccess() {
        return new ParametrizedReadOnlyCacheAccess<DownloadCacheDTO, AbstractParsingCacheDTO, ValidationCacheDTO>() {
            @Override
            public DownloadCacheDTO getDownloadInfoRecord(CacheKey key) {
                return new DownloadCacheDTO();
            }
            @Override
            public AbstractParsingCacheDTO getParsingInfoRecord(CacheKey key) {
                return new AbstractParsingCacheDTO() {
                    private static final long serialVersionUID = -8385552420652856448L;

                    @Override
                    public List<String> getStructureValidationMessages() {
                        return super.getStructureValidationMessages();
                    }

                    @Override
                    public void setStructureValidationMessages(List<String> structureValidationMessages) {
                        super.setStructureValidationMessages(structureValidationMessages);
                    }
                };
            }
            @Override
            public ValidationCacheDTO getValidationInfoRecord(CacheKey key) {
                return new ValidationCacheDTO();
            }
            @Override
            public Set<CacheKey> getAllCacheKeys() {
                return Collections.emptySet();
            }
        };
    }

}

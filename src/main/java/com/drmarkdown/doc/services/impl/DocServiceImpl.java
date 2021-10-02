package com.drmarkdown.doc.services.impl;

import com.drmarkdown.doc.dtos.DocDto;
import com.drmarkdown.doc.exceptions.UserNotAllowedException;
import com.drmarkdown.doc.models.DocModel;
import com.drmarkdown.doc.repositories.DocRepository;
import com.drmarkdown.doc.services.DocService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Objects.isNull;

@Service
public class DocServiceImpl implements DocService {

    @Autowired
    private DocRepository docRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void updateDoc(DocDto docDto, String userId) throws UserNotAllowedException {
        Optional<DocModel> optionalDocModel = docRepository.findById(docDto.getId());
        if (optionalDocModel.isPresent()) {
            DocModel docModel = optionalDocModel.get();
            if (docModel.getUserId().equals(userId)) {
                docModel.mapDto(docDto);
                docRepository.save(docModel);
            } else {
                throw new UserNotAllowedException("You are not allowed to modify this document");
            }
        } else {
            throw new NoSuchElementException("No document with Id "+ docDto.getId() + " was found.");
        }
    }

    @Override
    public List<DocDto> fetchTopRecentDocs() {
        final Page<DocModel> docModels = docRepository.findAllByAvailable(true, PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "updatedAt")));
        return docModels.stream()
                .map(docModel -> {
                    DocDto docDto = new DocDto();
                    docDto.mapEntityToDto(docModel);
                    return docDto;
                }).collect(Collectors.toList());
    }

    @Override
    public DocDto fetchDoc(String docId, String userId) {
        Optional<DocModel> optionalDocModel = docRepository.findById(docId);
        AtomicReference<DocDto> docDto = new AtomicReference<DocDto>();
        optionalDocModel.ifPresent(docModel -> {
            if (optionalDocModel.get().getUserId().equals(userId)) {
                docDto.set(new DocDto());
                docDto.get().mapEntityToDto(docModel);
            }
        });
        return docDto.get();
    }

    @Override
    public List<DocDto> fetchDocForUser(String userId, String callerUserId) {
        List<DocModel> models = docRepository.findAllByUserIdOrderByUpdatedAtDesc(userId);
        return models.stream()
//                .filter(docModel -> docModel.getUserId().equals(callerUserId))
                .map(docModel -> {
                    DocDto docDto = new DocDto();
                    docDto.mapEntityToDto(docModel);
                    return docDto;
                }).collect(Collectors.toList());
    }

    @Override
    public void createDocument(DocDto docDto) {
        checkNotNull(docDto.getContent());
        checkNotNull(docDto.getTitle());
        checkNotNull(docDto.getUserId());
        DocModel docModel = new DocModel(docDto);

        if (isNull(docModel.getAvailable())) {
            docModel.setAvailable(false);
        }

        docRepository.save(docModel);

        docDto.mapEntityToDto(docModel);
    }

    @Override
    public void deleteDoc(String userId, String docId) {
        var doc = docRepository.findById (docId);
        doc.ifPresent (docModel -> {
            if (!docModel.getUserId ().equals (userId)) {
                throw new RuntimeException ("User does not own this doc");
            }
            docRepository.delete (docModel);
        });
        doc.orElseThrow (() -> new RuntimeException ("Unable to delete"));
    }
}
